package test;

import database.EnvLoader;

public class TestEnvLoader {
    public static void main(String[] args) {
        EnvLoader.load();
        String user = EnvLoader.get("SPEEDER_ADMINISTRADOR_USER");
        System.out.println("SPEEDER_ADMINISTRADOR_USER='" + user + "'");
    }
}
