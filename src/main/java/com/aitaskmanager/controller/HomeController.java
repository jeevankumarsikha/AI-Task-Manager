package com.aitaskmanager.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public landing page so visiting the root URL shows a friendly status page
 * (instead of a 401) confirming the API is running and listing its endpoints.
 */
@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>AI-Task-Manager API</title>
              <style>
                body { font-family: system-ui, -apple-system, Segoe UI, Roboto, sans-serif;
                       max-width: 760px; margin: 48px auto; padding: 0 20px; color: #1a1a2e;
                       line-height: 1.6; }
                .badge { display: inline-block; background: #16a34a; color: #fff;
                         padding: 4px 12px; border-radius: 999px; font-size: 14px; font-weight: 600; }
                h1 { margin-bottom: 4px; }
                code { background: #f1f5f9; padding: 2px 6px; border-radius: 4px; font-size: 14px; }
                table { border-collapse: collapse; width: 100%; margin-top: 12px; }
                th, td { text-align: left; padding: 8px 10px; border-bottom: 1px solid #e2e8f0; font-size: 14px; }
                th { color: #475569; }
                .method { font-weight: 700; color: #2563eb; }
                a { color: #2563eb; }
                footer { margin-top: 32px; color: #64748b; font-size: 13px; }
              </style>
            </head>
            <body>
              <p class="badge">● API is live</p>
              <h1>AI-Task-Manager</h1>
              <p>A JWT-secured task management REST API built with <strong>Java 17 &amp; Spring Boot 3</strong>,
                 with a pluggable AI summarization endpoint. This is a backend service &mdash; use the
                 endpoints below with a REST client (Postman / curl).</p>

              <h3>Public endpoints</h3>
              <table>
                <tr><th>Method</th><th>Path</th><th>Description</th></tr>
                <tr><td class="method">POST</td><td><code>/api/auth/register</code></td><td>Create an account, returns a JWT</td></tr>
                <tr><td class="method">POST</td><td><code>/api/auth/login</code></td><td>Authenticate, returns a JWT</td></tr>
                <tr><td class="method">POST</td><td><code>/api/ai/summarize</code></td><td>Summarize a task description</td></tr>
              </table>

              <h3>Protected endpoints <span style="font-weight:400;font-size:13px;color:#64748b">(require <code>Authorization: Bearer &lt;token&gt;</code>)</span></h3>
              <table>
                <tr><th>Method</th><th>Path</th><th>Description</th></tr>
                <tr><td class="method">POST</td><td><code>/api/tasks</code></td><td>Create a task</td></tr>
                <tr><td class="method">GET</td><td><code>/api/tasks</code></td><td>List your tasks</td></tr>
                <tr><td class="method">GET</td><td><code>/api/tasks/{id}</code></td><td>Get a task</td></tr>
                <tr><td class="method">PUT</td><td><code>/api/tasks/{id}</code></td><td>Update a task</td></tr>
                <tr><td class="method">DELETE</td><td><code>/api/tasks/{id}</code></td><td>Delete a task</td></tr>
                <tr><td class="method">GET</td><td><code>/api/tasks/search?status=TODO</code></td><td>Filter by status</td></tr>
              </table>

              <footer>
                Built AI-first with Claude, Cursor, GitHub Copilot &amp; ChatGPT &middot;
                <a href="https://github.com/jeevankumarsikha/AI-Task-Manager">Source on GitHub</a>
              </footer>
            </body>
            </html>
            """;
    }
}
