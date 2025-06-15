# ------------------------------------------------------------
# Layer 1. Base Image Specification
# ------------------------------------------------------------
# Uses an official Maven image that comes pre-installed with OpenJDK 11 and Maven 3.6.3.
# This ensures a consistent build environment and eliminates the need to manually install Java or Maven.
# It's ideal for lightweight containers and CI/CD pipelines.
FROM maven:3.6.3-openjdk-11

# ------------------------------------------------------------
# Layer 2. Set the Working Directory Inside the Container
# ------------------------------------------------------------
# Defines `/app` as the working directory where all subsequent commands will run.
# If the folder does not exist, Docker will create it automatically.
WORKDIR /app

# ------------------------------------------------------------
# Layer 3. Copy Only the pom.xml to the Container
# ------------------------------------------------------------
# Copies the `pom.xml` file (Maven's configuration file) from the host to the container.
# This is done before copying the rest of the source code to maximize Docker layer caching.
# If only the source code changes but `pom.xml` remains the same, Docker will reuse the cached dependencies.
COPY pom.xml .

# ------------------------------------------------------------
# Layer 4. Download Project Dependencies
# ------------------------------------------------------------
# This command downloads all dependencies declared in the `pom.xml` file.
# The `-B` flag stands for "batch mode", which disables any interactive prompts—essential for CI/CD environments.
# This step does NOT compile the code; it ensures dependencies are cached for faster builds.
RUN mvn dependency:go-offline -B

# ------------------------------------------------------------
# Layer 5. Copy the Remaining Project Source Code
# ------------------------------------------------------------
# Copies the entire `src` folder (where your actual source code resides) into the container.
# This is done after downloading dependencies to prevent Docker from invalidating the cache when the code changes.
COPY src ./src

# ------------------------------------------------------------
#Layer 6 6. Compile and Package the Application (Skip Tests)
# ------------------------------------------------------------
# Compiles the source code and packages it into a `.jar` file using Maven.
# The `-DskipTests=true` flag skips running tests during this phase—useful for speeding up build pipelines.
# The `-B` flag ensures batch mode (non-interactive).
RUN mvn clean package -DskipTests=true -B

# ------------------------------------------------------------
# 7. Default Command When Container Starts
# ------------------------------------------------------------
# Defines the default command that runs when the container is started.
# Here, it runs `mvn test`, executing all unit tests defined in your project.
CMD ["mvn", "test"]
