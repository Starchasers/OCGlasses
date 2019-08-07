package com.bymarcin.openglasses.integration.opensecurity;

import pcl.opensecurity.common.protection.Protection;

public class OpenSecurity {
    public static boolean isCompatible(){
        try {
            return Protection.class.getMethod("getAllAreas") != null;
        } catch (Exception ex){
            return false;
        }
    }
}
