package com.github.gsdocker.gsrpc;

/**
 * Created by liyang on 15/5/25.
 */
public interface Callback {
    /**
    * get callback timeout value
    * @return timeout duration which unit is second
    */
   int getTimeout();

   /**
    * rpc call return handler
    * @param e
    * @param callReturn
    */
   void Return(Exception e,com.github.gsdocker.gsrpc.Return callReturn);
}
