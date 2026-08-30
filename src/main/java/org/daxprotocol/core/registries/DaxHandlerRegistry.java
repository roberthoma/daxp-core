package org.daxprotocol.core.registries;

import org.daxprotocol.core.exceptions.DaxExecutorException;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/// RULES
// One   DaxpHandler ca be use with one message
public class DaxHandlerRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DaxHandlerRegistry.class);
    /*****************************************************
     *  Handler And controller maps
     */

    Map<String, Method>     coreHandlerMap   = new ConcurrentHashMap<>();
    Map<String, Method>     handlerMap       = new ConcurrentHashMap<>();
    Map<Class<?>, Object >  daxControllerMap = new ConcurrentHashMap<>();



    public void putHandler(String msgType, Method method, Class<?> clazz) {
        handlerMap.put(msgType, method);
    }

    public void registerCtrl(Object daxpController) {
        daxControllerMap.put(daxpController.getClass(), daxpController);
    }

    private void exeMsg(DaxMessage message, DaxFrame respFrame, Map<String, Object> txContext) {
        String msgType = message.getMsgType();
        Method method = handlerMap.get(msgType);

        if (method == null) {
            throw new DaxExecutorException("No handler registered for message type: " + msgType);
        }

        Object obj = daxControllerMap.get(method.getDeclaringClass());
        if (obj == null) {
            throw new DaxExecutorException("No controller instance found for " + method.getDeclaringClass().getName());
        }

        logger.trace("Executing handler method: {}", method.getName());

        try {
            // Inspect method signature instead of catching invocation failures
            if (method.getParameterCount() == 3) {
                method.invoke(obj, message, respFrame, txContext);
            } else {
                method.invoke(obj, message, respFrame);
            }
        } catch (InvocationTargetException e) {
            // Unwrap to reveal the actual exception thrown inside your controller
            Throwable targetException = e.getCause();
            throw new DaxExecutorException("Error executing message handler for " + msgType,
                    targetException != null ? targetException : e);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            // Reflection setup error (wrong args, private method access, etc.)
            throw new DaxExecutorException("Reflection invocation failed for " + msgType, e);
        }
    }

    public void executor( DaxFrame reqFrame, DaxFrame respFrame){
            logger.trace("Start executor");
            Map<String, Object> txContext = new HashMap<>();
            reqFrame.getAllMessage().forEach(msg -> exeMsg(msg, respFrame, txContext));
    }

}


/*
public class DaxHandlerRegistry {

    private final Map<String, Method> handlerMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> daxControllerMap = new ConcurrentHashMap<>();

    public void putHandler(String msgType, Method method) {
        handlerMap.put(msgType, method);
    }

    public void registerCtrl(Object daxpController) {
        daxControllerMap.put(daxpController.getClass(), daxpController);
    }

    public DaxMessage executor(DaxMessage reqMsg) {
        String msgType = reqMsg.getMsgType();

        try {
            // 1. Check if handler exists
            Method method = handlerMap.get(msgType);
            if (method == null) {
                // Throw our custom "Unknown Tag/Message" exception
                throw new DAXPDictionaryException(String.format("No handler registered for MsgType: %s", msgType));
            }

            // 2. Find the instance of the Controller
            Object controllerInstance = daxControllerMap.get(method.getDeclaringClass());
            if (controllerInstance == null) {
                throw new DAXPException("DAXP-0020", "Controller instance not found for method: " + method.getName());
            }

            // 3. Invoke and Cast
            Object respObj = method.invoke(controllerInstance, reqMsg);

            if (!(respObj instanceof DaxMessage)) {
                throw new DAXPException("DAXP-0021", "Handler did not return a DaxMessage object.");
            }

            return (DaxMessage) respObj;

        } catch (InvocationTargetException e) {
            // This catches exceptions thrown INSIDE your controller method (Business Logic Errors)
            Throwable cause = e.getCause();
            throw new DAXPAppException(5000, "Business Logic Error: " + cause.getMessage(), cause);
        } catch (Exception e) {
            // Catch Reflection/System errors
            throw new DAXPException("DAXP-0099", "Execution failed: " + e.getMessage(), e);
        }
    }
}

        */
