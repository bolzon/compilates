package com.compiler.status;

public class StatusTransfer {

    private StatusManager statusManager;
    private static StatusTransfer instance;

    static {
        instance = new StatusTransfer();
    }

    private StatusTransfer() {
        statusManager = null;
    }

    public static StatusTransfer getInstance() {
        return instance;
    }

    public void setStatusManager(StatusManager statusManager) {
        this.statusManager = statusManager;
    }

    public StatusManager getStatusManager() {
        return statusManager;
    }
}
