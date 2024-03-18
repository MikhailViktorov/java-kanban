package server;

import adapters.DurationAdapter;
import adapters.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exceptions.IntersectionException;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;


    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        this.gson = gsonBuilder.create();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/subtasks", new SubtasksHandler());
        server.createContext("/epics", new EpicsHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedTasksHandler());
    }

    public void start() {
        System.out.println("Go to http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    private class TasksHandler implements HttpHandler {
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
                            httpExchange.sendResponseHeaders(200, 0);
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
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    private class SubtasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String path = httpExchange.getRequestURI().toString();
                String requestMethod = httpExchange.getRequestMethod();
                switch (requestMethod) {
                    case "GET": {
                        if (Pattern.matches("^/subtasks$", path)) {
                            String response = gson.toJson(taskManager.getAllSubtasks());
                            sendText(httpExchange, response);
                            break;
                        }
                        if (Pattern.matches("^/subtasks/\\d$", path)) {
                            String[] pathId = path.split("/");
                            int taskId = parsePathId(pathId[2]);
                            if (taskId != -1) {
                                String response = gson.toJson(taskManager.getSubtaskById(taskId));
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
                        if (Pattern.matches("^/subtasks$", path)) {
                            taskManager.deleteAllSubtasks();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }

                        if (Pattern.matches("^/subtasks/\\d$", path)) {
                            String[] pathId = path.split("/");
                            int taskId = parsePathId(pathId[2]);
                            if (taskId != -1) {
                                taskManager.deleteSubtaskById(taskId);
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
                        if (Pattern.matches("^/subtasks/\\d$", path)) {
                            String body = readText(httpExchange);
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            taskManager.updateSubtasks(subtask);
                            httpExchange.sendResponseHeaders(201, 0);
                        }

                        if (Pattern.matches("^/subtasks$", path)) {
                            String body = readText(httpExchange);
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            taskManager.createSubtask(subtask);
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
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    private class EpicsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String path = httpExchange.getRequestURI().toString();
                String requestMethod = httpExchange.getRequestMethod();
                switch (requestMethod) {
                    case "GET": {
                        if (Pattern.matches("^/epics$", path)) {
                            String response = gson.toJson(taskManager.getAllEpics());
                            sendText(httpExchange, response);
                            break;
                        }
                        if (Pattern.matches("^/epics/\\d/subtasks$", path)) {
                            String[] pathId = path.split("/");
                            int epicId = parsePathId(pathId[2]);
                            if (epicId != -1) {
                                String response = gson.toJson(taskManager.getEpicById(epicId).getSubtaskList());
                                sendText(httpExchange, response);
                            } else {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                            break;
                        }
                        if (Pattern.matches("^/epics/\\d$", path)) {
                            String[] pathId = path.split("/");
                            int taskId = parsePathId(pathId[2]);
                            if (taskId != -1) {
                                String response = gson.toJson(taskManager.getEpicById(taskId));
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
                        if (Pattern.matches("^/epics$", path)) {
                            taskManager.deleteAllEpics();
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        }

                        if (Pattern.matches("^/epics/\\d$", path)) {
                            String[] pathId = path.split("/");
                            int taskId = parsePathId(pathId[2]);
                            if (taskId != -1) {
                                taskManager.deleteEpicById(taskId);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        } else {
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    case "POST": {
                        if (Pattern.matches("^/epics$", path)) {
                            String body = readText(httpExchange);
                            Epic epic = gson.fromJson(body, Epic.class);
                            taskManager.createEpic(epic);
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
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    private class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String path = httpExchange.getRequestURI().toString();
                String requestMethod = httpExchange.getRequestMethod();
                if (requestMethod.equals("GET") && Pattern.matches("^/history$", path)) {
                    String response = gson.toJson(taskManager.getHistory());
                    sendText(httpExchange, response);
                } else {
                    System.out.println("invalid request method " + requestMethod);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    private class PrioritizedTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String path = httpExchange.getRequestURI().toString();
                String requestMethod = httpExchange.getRequestMethod();
                if (requestMethod.equals("GET") && Pattern.matches("^/prioritized$", path)) {
                    String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(httpExchange, response);
                } else {
                    System.out.println("invalid request method " + requestMethod);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes();
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }
}
