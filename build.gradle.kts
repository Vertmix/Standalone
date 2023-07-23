plugins {
    id("java")
}

group = "com.cjcameron92.games"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("redis.clients:jedis:4.4.3")
    implementation("com.google.guava:guava:32.1.1-jre")
    implementation("org.tinylog:tinylog:0.8.1")
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}

tasks.test {
    useJUnitPlatform()
}