package by.losik.lab1;

import java.util.Scanner;

public abstract class EntityCRUD {
    protected ContextImpl contextImpl;
    protected Scanner scanner;
    public EntityCRUD(ContextImpl context, Scanner scanner){
        this.contextImpl = context;
        this.scanner = scanner;
    }

    public abstract void create();
    public abstract void read();
    public abstract void delete();
}
