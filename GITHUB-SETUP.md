# 📦 How to Push spring-user-service to GitHub

Follow these steps exactly — takes about 5 minutes.

---

## STEP 1 — Create the GitHub Repository

1. Go to → https://github.com/new
2. Fill in:
   - **Repository name:** `spring-user-service`
   - **Description:** `Spring Boot 2.7.x User Management API — migration test target`
   - **Visibility:** Public (so SpringShift can fetch it without a token)
   - ❌ Do NOT check "Add README" or "Add .gitignore" (we already have them)
3. Click **"Create repository"**
4. Copy the repo URL shown — it will look like:
   `https://github.com/YOUR_USERNAME/spring-user-service.git`

---

## STEP 2 — Set Up Git Locally

Open your terminal and run these commands one by one:

```bash
# Navigate into the project folder
cd spring-user-service

# Initialize git
git init

# Add all files
git add .

# First commit
git commit -m "feat: Spring Boot 2.7.x User Management API (pre-migration sample)"

# Set main as default branch
git branch -M main

# Connect to your GitHub repo (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/spring-user-service.git

# Push to GitHub
git push -u origin main
```

---

## STEP 3 — Verify on GitHub

1. Open `https://github.com/YOUR_USERNAME/spring-user-service`
2. You should see all these files:
   ```
   ✔ pom.xml
   ✔ README.md
   ✔ .gitignore
   ✔ src/main/java/com/example/userservice/
       ✔ UserServiceApplication.java
       ✔ controller/UserController.java
       ✔ service/UserService.java
       ✔ repository/UserRepository.java
       ✔ entity/User.java
       ✔ entity/AuditableEntity.java
       ✔ dto/CreateUserRequest.java
       ✔ dto/UpdateUserRequest.java
       ✔ dto/UserResponse.java
       ✔ dto/ApiResponse.java
       ✔ security/SecurityConfig.java
       ✔ security/CustomUserDetailsService.java
       ✔ config/SwaggerConfig.java
       ✔ config/DataInitializer.java
       ✔ exception/GlobalExceptionHandler.java
       ✔ exception/ResourceNotFoundException.java
       ✔ exception/DuplicateResourceException.java
   ✔ src/main/resources/application.properties
   ✔ src/test/java/com/example/userservice/
       ✔ UserServiceTest.java
       ✔ UserControllerIntegrationTest.java
   ```

---

## STEP 4 — Test with SpringShift

Open SpringShift-MigrationTool.html in your browser, then:

1. Click the **"GitHub / GitLab URL"** tab
2. Paste your repo URL:
   ```
   https://github.com/YOUR_USERNAME/spring-user-service
   ```
3. Leave the token blank (public repo)
4. Click **"Fetch Repo"**
5. In the file explorer, all Java files will be auto-selected
6. Click **"✔ Use Selected"**
7. Click **"⚡ Start Migration"**

SpringShift will then:
- Detect: Spring Boot 2.7.18, Java 11, Maven
- Scan: 3 CVEs (jackson, snakeyaml, commons-text)
- Migrate: pom.xml → Spring Boot 3.5.0 + Java 21
- Fix: All 14 javax.* → jakarta.* imports across 6 files
- Fix: SecurityConfig.java (WebSecurityConfigurerAdapter → SecurityFilterChain)
- Fix: antMatchers → requestMatchers, authorizeRequests → authorizeHttpRequests
- Fix: LocalServerPort import in test
- Fix: application.properties renamed key
- Download: All migrated files + full report

---

## STEP 5 — (Optional) Private Repo Setup

If you want to make the repo private:

1. GitHub → repo → Settings → scroll to "Danger Zone" → Change visibility → Private
2. In SpringShift, generate a token:
   - GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
   - Click "Generate new token (classic)"
   - Check scope: `repo`
   - Copy the token
3. Paste the token in the SpringShift **"Personal Access Token"** field

---

## Troubleshooting

| Problem | Fix |
|---|---|
| `git push` asks for password | Use a Personal Access Token instead of your password |
| `remote: Repository not found` | Check the remote URL — `git remote -v` |
| SpringShift shows 0 files | Confirm repo is public, or add token for private |
| `403 Forbidden` on fetch | Token is missing or expired |
| Java files not showing in explorer | They are there — scroll down or click "Select All Java" |
