package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import models.Task;
import server.HttpTaskServer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class TasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().toString();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(taskManager.getAllTasks());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/\\d$", path)) {
                        String[] pathId = path.split("/");
                        int taskId = parsePathId(pathId[2]);
                        if (taskId != -1) {
                            String response = gson.toJson(taskManager.getTaskById(taskId));
                            sendText(httpExchange, response);
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(500, 0);
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks$", path)) {
                        taskManager.deleteAllTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }

                    if (Pattern.matches("^/tasks/\\d$", path)) {
                        String[] pathId = path.split("/");
                        int taskId = parsePathId(pathId[2]);
                        if (taskId != -1) {
                            taskManager.deleteTaskById(taskId);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {

                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/\\d$", path)) {
                        String body = readText(httpExchange);
                        Task task = gson.fromJson(body, Task.class);
                        taskManager.updateTask(task);
                        httpExchange.sendResponseHeaders(201, 0);
                        break;
                    }
                    if (Pattern.matches("^/tasks$", path)) {
                        String body = readText(httpExchange);
                        Task task = gson.fromJson(body, Task.class);
                        taskManager.createTask(task);
                        httpExchange.sendResponseHeaders(201, 0);
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("invalid request method " + requestMethod);
                }
            }

        } catch (Exception e) {
            httpExchange.sendResponseHeaders(404, 0);
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes();
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }
}