package com.lp.demo.spring.event.another;


public class AEvent extends Event {

    private static final long serialVersionUID = -8806838352922851813L;

    private String name;

    public AEvent(Object source) {
        super(source);
        this.name = this.getClass().getSimpleName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AEvent{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
