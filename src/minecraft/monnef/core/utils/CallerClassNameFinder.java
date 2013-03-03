package monnef.core.utils;

public class CallerClassNameFinder {
    private final static MySecurityManager manager = new MySecurityManager();

    public String getCallerClassName(int callStackDepth) {
        return manager.getCallerClassName(callStackDepth);
    }

    static class MySecurityManager extends SecurityManager {
        public String getCallerClassName(int callStackDepth) {
            return getClassContext()[callStackDepth].getName();
        }
    }
}
