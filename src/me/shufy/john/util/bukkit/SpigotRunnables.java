package me.shufy.john.util.bukkit;

import me.shufy.john.Main;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class SpigotRunnables {
    public static final Main plugin = Main.getPlugin(Main.class);
    // cancels a task later
    public static void cancelTaskLater(BukkitTask task, int secondsLater) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.cancel();
            }
        }.runTaskLater(plugin, (20L * secondsLater));
    }
    // you can't run methods from different classes they must all be within the same class AND accessible. Returns a hashmap of the method and its respective return value
    // if the methods have args it will throw exceptions and freak out.. this is only made for non arg methods
    public static  HashMap<Method, Object> runMethodsLater(Class<?> instance, int secondsLater, Method... methods) {
        HashMap<Method, Object> methodReturnValues = new HashMap<>();
        for (Method method : methods) {
            method.setAccessible(true);
            BukkitTask runLaterTask = new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Object returnValue = method.invoke(instance);
                        methodReturnValues.put(method, returnValue);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskLater(plugin, (20L * secondsLater));
        }
        return methodReturnValues;
    }
}
