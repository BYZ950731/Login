package com.tonmx.inspection;

import java.util.List;

public class FormItem {
    private List<Todo> todo;
    private List<Hand> hand;
    public void setTodo(List<Todo> todo) {
        this.todo = todo;
    }
    public List<Todo> getTodo() {
        return todo;
    }

    public void setHand(List<Hand> hand) {
        this.hand = hand;
    }
    public List<Hand> getHand() {
        return hand;
    }
}
