# docker build -f src/main/docker/Dockerfile.builder -t quarkus-native-builder .
#  mvn package -Dnative -Dquarkus.native.container-build=true -Dquarkus.native.builder-image=quarkus-native-builder -Dquarkus.native.builder-image.pull=never

FROM quay.io/quarkus/ubi9-quarkus-mandrel-builder-image:jdk-23

USER root
RUN microdnf install -y libXxf86vm pango mesa-libGL && microdnf clean all
USER 1000