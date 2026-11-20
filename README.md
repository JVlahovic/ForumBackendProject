# Forum Backend

Spring Boot backend for a full-stack forum application. Provides authentication, role-based
authorization, thread/post management, search, and an admin API — with a MySQL database whose
schema and seed data are managed by Liquibase.

> Built as the final project for the Logate Academy 2025–2026 course.

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.5 |
| Security | Spring Security + JJWT (HS256), BCrypt (strength 12) |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL 8+ |
| Migrations | Liquibase 5.x |
| Mapping | MapStruct 1.6 |
| Boilerplate | Lombok |
| Mail | Spring Mail (Gmail SMTP) |
| Build | Maven Wrapper (`mvnw`) |
| Runtime | Docker / Docker Compose |

---

## Features

- **Auth:** register with email verification, login (JWT), password reset (code → new password),
  change password.
- **Roles & capabilities (DB-driven):** Guest, User, Moderator, Admin. Each role stores a JSON
  capability set used for authorization.
- **Categories:** view gated by `access_ctrl`, posting gated by `post_ctrl`.
- **Threads:** create, list by category, read one, pin/lock/delete (moderator/admin).
- **Posts:** paginated replies, create, edit own post, delete own post (moderator/admin can delete any).
- **Global thread search:** authenticated, title search across categories, filtered by the caller's
  view access.
- **Admin API:** user list with username/role filters, ban/unban, role assignment; category create/delete.
- **User profile:** `/me` profile, threads started by the user.
- **Housekeeping:** scheduled deletion of expired unverified registrations.

---

## Requirements

- **Java 21**
- **MySQL 8+** (local or containerized)
- **Docker Desktop** (optional, for the containerized workflow)
- The Maven wrapper is included — no system Maven required.

---

## Environment Configuration

The application reads configuration from environment variables. Locally, `spring-dotenv` loads a
root-level **`.env`** file automatically.

### `.env` (required, gitignored)

Create `.env` in the project root. Never commit it.

| Variable | Required | Description | Example |
|----------|----------|-------------|---------|
| `DB_URL` | yes | JDBC URL to the database | `jdbc:mysql://localhost:3306/forumprojectdb` |
| `DB_USERNAME` | yes | Database user | `forum` |
| `DB_PASSWORD` | yes | Database password | `change-me` |
| `MAIL_HOST` | yes | SMTP host | `smtp.gmail.com` |
| `MAIL_EMAIL` | yes | SMTP username / sender address | `you@gmail.com` |
| `MAIL_PASSWORD` | yes | SMTP password (Gmail **app password**) | `abcd efgh ijkl mnop` |
| `JWT_SECRET_KEY` | yes | HMAC signing key (≥ 32 chars for HS256) | `a-long-random-string` |
| `JWT_VALIDITY_TIME` | yes | Access token lifetime, in **minutes** | `120` |
| `JWT_REFRESH_TIME` | yes | Refresh lifetime (reserved) | `1440` |
| `ADMIN_USERNAME` | no | Bootstrap admin username (created on first boot if no admin exists) | `admin` |
| `ADMIN_PASSWORD` | no | Bootstrap admin password | `change-me` |
| `ADMIN_EMAIL` | no | Bootstrap admin email | `admin@example.com` |
| `CORS_ALLOWED_ORIGINS` | no | Comma-separated allowed origins | `http://localhost:4200,https://my-app.vercel.app` |

Example `.env`:

```env
DB_URL=jdbc:mysql://localhost:3306/forumprojectdb
DB_USERNAME=forum
DB_PASSWORD=change-me

MAIL_HOST=smtp.gmail.com
MAIL_EMAIL=you@gmail.com
MAIL_PASSWORD=your-app-password

JWT_SECRET_KEY=replace-with-a-long-random-string
JWT_VALIDITY_TIME=120
JWT_REFRESH_TIME=1440

ADMIN_USERNAME=admin
ADMIN_PASSWORD=change-me
ADMIN_EMAIL=admin@example.com

CORS_ALLOWED_ORIGINS=http://localhost:4200
```

### `liquibase.properties` (local only, gitignored)

Used **only** by the Liquibase Maven plugin (`generateChangeLog`, `status`, etc.). The running
application does **not** need it — it loads `application.yaml` → `spring.liquibase.change-log`.
Maven does **not** read `.env`, so the connection must be repeated here.

```properties
changeLogFile=src/main/resources/db/changelog/db.changelog-master.yaml
url=jdbc:mysql://localhost:3306/forumprojectdb
username=forum
password=change-me
driver=com.mysql.cj.jdbc.Driver
```

### Commit these templates, never the real files

- `liquibase.properties.example` (placeholders)
- `.env.example` (placeholders)

`.gitignore` already excludes `.env` and `liquibase.properties`.

---

## Running Locally

```bash
# from the project root
./mvnw spring-boot:run
```

Or run `ForumBackendApplication` from your IDE. The server listens on
**http://localhost:8090**.

On startup the app runs Liquibase automatically: it creates the schema and seeds roles/categories
if they are missing.

Build a jar:

```bash
./mvnw clean package
java -jar target/forum-backend-*.jar
```

---

## Database & Liquibase

Changelogs live in `src/main/resources/db/changelog/`:

```
db.changelog-master.yaml   # includes the two below
schema.yaml                # generated 1:1 baseline of the database
seed.yaml                  # roles + categories (explicit IDs)
```

- The **application** applies pending changesets on startup.
- **Forward-only:** never edit already-applied changesets; add a new one instead.
- Seeded roles (explicit IDs): `1 Admin`, `2 Moderator`, `3 User`, `4 Guest (default)`.
- Seeded categories: `General Forum`, `Rules`, `Moderator Panel`, `Announcements`.

Useful Maven plugin commands (requires `liquibase.properties`):

```bash
./mvnw liquibase:status
./mvnw liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/schema.yaml
./mvnw liquibase:update
./mvnw liquibase:changelogSync
```

> **Runner policy:** use the application as the single migration runner. Mixing the Maven plugin
> and the app can produce filename-identity mismatches in `DATABASECHANGELOG`.

---

## Docker

The backend ships with a multi-stage `Dockerfile` and a `docker-compose.yaml`.
Create `.env` first, then:

```bash
docker compose up --build -d
docker compose logs -f backend
docker compose down
```

The compose file targets a **host MySQL** via `host.docker.internal`. On Linux/WSL this is enabled
by the `extra_hosts: host.docker.internal:host-gateway` entry.

### Local MySQL prerequisites for the container

1. MySQL listens on all interfaces (`bind-address = 0.0.0.0`, or unset → default `*`).
2. A user that the container can authenticate as:

```sql
CREATE USER 'forum'@'%' IDENTIFIED BY 'change-me';
GRANT ALL PRIVILEGES ON forumprojectdb.* TO 'forum'@'%';
FLUSH PRIVILEGES;
```

3. Windows Firewall allows inbound TCP 3306 from the Docker/WSL network.

---

## API Overview

### Authentication (public)

| Method | Path |
|--------|------|
| POST | `/api/register` |
| POST | `/api/login` |
| POST | `/api/emailVerify/verify` |
| POST | `/api/emailVerify/resend` |
| POST | `/api/passwordReset/request` |
| POST | `/api/passwordReset/confirm` |

### Categories

| Method | Path | Access |
|--------|------|--------|
| GET | `/api/thread-categories` | public (filtered by role) |
| POST | `/api/thread-categories` | admin |
| DELETE | `/api/thread-categories/{id}` | admin |

### Threads

| Method | Path | Access |
|--------|------|--------|
| GET | `/api/threads/category/{categoryId}` | public (access-checked) |
| POST | `/api/threads` | authenticated + post rights |
| GET | `/api/threads/{threadId}` | authenticated |
| DELETE | `/api/threads/{threadId}` | moderator/admin |
| PATCH | `/api/threads/pin/{threadId}` | moderator/admin |
| PATCH | `/api/threads/lock/{threadId}` | moderator/admin |
| GET | `/api/threads/search?title=` | authenticated, access-filtered |

### Posts

| Method | Path | Access |
|--------|------|--------|
| GET | `/api/threads/{threadId}/posts` | authenticated |
| POST | `/api/threads/{threadId}/posts` | authenticated + post rights |
| PUT | `/api/posts/{postId}` | author |
| DELETE | `/api/posts/{postId}` | author, moderator, admin |

### Users & Admin

| Method | Path | Access |
|--------|------|--------|
| GET | `/api/users/me` | authenticated |
| GET | `/api/users/me/threads` | authenticated |
| PUT | `/api/users/me/password` | authenticated |
| GET | `/api/adminpanel/users?username=&roleId=` | admin |
| GET | `/api/adminpanel/roles` | admin |
| PATCH | `/api/adminpanel/users/{id}/ban` | admin |
| PATCH | `/api/adminpanel/users/{id}/unban` | admin |
| PUT | `/api/adminpanel/users/{id}/role` | admin |

---

## Roles & Permissions

Authorization is **DB-driven**. Each role's `permissions` JSON holds a `capabilities` set; the
JWT filter loads the user's authorities fresh on every request.

| Role | Capabilities (summary) |
|------|------------------------|
| Admin | all (`post:*`, `thread:*`, `user:*`, `view:*`) |
| Moderator | most posting/moderation + `view:moderator` |
| User | `post:registered`, `post:delete_own`, `view:registered` |
| Guest (default) | `view:everyone` |

Category rules: `access_ctrl` decides who can *view*; `post_ctrl` decides who can *post*. Thread
moderation (pin/lock/delete) and category management are restricted to ADMIN (moderation endpoints
also accept MODERATOR).

---

## Project Structure

```
src/main/java/com/january0001/project/forumbackend/
├── config/          # CORS config
├── controller/      # threads, posts, categories, admin, users
├── converter/       # JSON <-> Permissions converter
├── dto/             # command / query / gate DTOs
├── entity/          # User, Role, Thread, ThreadCategory, Post
├── filter/          # search filter DTOs
├── handler/         # global exception handler
├── mapper/          # MapStruct mappers
├── repository/      # Spring Data repositories
├── security/        # config, filter, services, DTOs, utils
├── service/         # business services
└── specification/   # JPA Specification builders (search)

src/main/resources/
├── application.yaml
└── db/changelog/    # Liquibase changelogs (master, schema, seed)
```

---

## Troubleshooting

| Symptom | Cause / Fix |
|---------|-------------|
| `Communications link failure` | MySQL down, wrong `DB_URL`, bind-address, or firewall |
| `Access denied for user` | Wrong `DB_USERNAME`/`DB_PASSWORD`, or user lacks `'user'@'%'` |
| App fails with `createTable ... already exists` | Running Liquibase against a schema it didn't baseline; don't mix CLI and app runners |
| 403 on every protected call | Missing/expired `Authorization: Bearer` token (frontend interceptor) |
| Emails not sent | Gmail requires an **app password**; check `MAIL_*` |
| Search returns nothing | User lacks access to the categories containing matches |
| `liquibase:status` says changelog not found | Missing `liquibase.properties` or wrong `changeLogFile` |

---

## License

Educational project — see `LICENSE`.