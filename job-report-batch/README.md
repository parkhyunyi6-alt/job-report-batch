# job-report-batch

Daily job recommendation report batch for collecting postings, scoring them against a career profile, saving results in PostgreSQL, rendering an HTML report, and sending it by email.

## Stack

- Java 21
- Spring Boot 3.5.6
- Gradle Kotlin DSL
- Spring Data JPA
- PostgreSQL
- Flyway
- Spring Boot Mail
- Validation

## Local setup

Create a local PostgreSQL database and user:

```sql
CREATE DATABASE job_report;
CREATE USER job_report_app WITH PASSWORD 'job_report_app';
GRANT ALL PRIVILEGES ON DATABASE job_report TO job_report_app;
```

Run with the local profile:

```bash
gradle bootRun --args='--spring.profiles.active=local'
```

Build a runnable jar:

```bash
gradle clean bootJar
```

Run the jar:

```bash
java -jar build/libs/job-report-batch-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

The application runs once, generates the daily report, sends email when mail configuration is complete, and exits.

## Environment variables

Local profile:

```bash
DB_LOCAL_PASSWORD=job_report_app
MAIL_HOST=smtp.example.com
MAIL_PORT=587
MAIL_USERNAME=your-smtp-user
MAIL_PASSWORD=your-smtp-password
MAIL_FROM=job-report@example.com
MAIL_TO=you@example.com
SARAMIN_API_KEY=optional-saramin-api-key
```

Production profile:

```bash
DB_PROD_URL=jdbc:postgresql://your-db-host:5432/job_report
DB_PROD_USERNAME=job_report_app
DB_PROD_PASSWORD=strong-password
MAIL_HOST=smtp.example.com
MAIL_PORT=587
MAIL_USERNAME=your-smtp-user
MAIL_PASSWORD=your-smtp-password
MAIL_FROM=job-report@example.com
MAIL_TO=you@example.com
SARAMIN_API_KEY=optional-saramin-api-key
```

If mail settings are missing, the HTML report is logged instead of failing the batch.

## EC2 cron example

```cron
0 8 * * * java -jar /home/ec2-user/apps/job-report/job-report.jar --spring.profiles.active=prod
```
