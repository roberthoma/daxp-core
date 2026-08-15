package org.daxprotocol.core.dispatcher;

import org.daxprotocol.core.exceptions.DaxExecutorException;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//rules
// One   DaxpHandler ca be use with one message
public class DaxHandlerRegistry {

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

    public void executor( DaxFrame reqFrame, DaxFrame respFrame){

        //TODO develop in message can be more that one message
        try {
            DaxMessage  reqMsg = reqFrame.getFirstMessage();

            String msgType = reqMsg.getMsgType();

            Method method  = handlerMap.get(msgType);

            Object obj = daxControllerMap.get(method.getDeclaringClass());

            method.invoke(obj, reqFrame, respFrame);

        } catch (Exception e) {
            throw new DaxExecutorException(e);
        }

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
