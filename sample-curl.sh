#!/usr/bin/env bash
# Sample curl commands for AI-Task-Manager
# Usage: start the app (mvn spring-boot:run) then run:  bash sample-curl.sh
set -e
BASE="http://localhost:8080"

echo "== 1. Register =="
curl -s -X POST "$BASE/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"jeevan","email":"jeevan@example.com","password":"secret123"}'
echo; echo

echo "== 2. Login (capture token) =="
TOKEN=$(curl -s -X POST "$BASE/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"jeevan","password":"secret123"}' | sed -n 's/.*"token":"\([^"]*\)".*/\1/p')
echo "TOKEN=$TOKEN"
echo

echo "== 3. Create a task =="
curl -s -X POST "$BASE/api/tasks" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Build backend","description":"Complete backend microservice implementation and testing","status":"TODO"}'
echo; echo

echo "== 4. Get all tasks =="
curl -s "$BASE/api/tasks" -H "Authorization: Bearer $TOKEN"
echo; echo

echo "== 5. Get task by id (1) =="
curl -s "$BASE/api/tasks/1" -H "Authorization: Bearer $TOKEN"
echo; echo

echo "== 6. Update task 1 =="
curl -s -X PUT "$BASE/api/tasks/1" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Build backend","description":"Updated description","status":"IN_PROGRESS"}'
echo; echo

echo "== 7. Search tasks by status =="
curl -s "$BASE/api/tasks/search?status=IN_PROGRESS" -H "Authorization: Bearer $TOKEN"
echo; echo

echo "== 8. AI summarize (public) =="
curl -s -X POST "$BASE/api/ai/summarize" \
  -H "Content-Type: application/json" \
  -d '{"description":"Complete backend microservice implementation and testing"}'
echo; echo

echo "== 9. Delete task 1 =="
curl -s -o /dev/null -w "HTTP %{http_code}\n" -X DELETE "$BASE/api/tasks/1" -H "Authorization: Bearer $TOKEN"
