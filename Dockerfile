FROM eclipse-temurin:21-jdk

RUN apt-get update && \
	apt-get install -y \
		git \
		maven \
	&& apt-get clean \
	&& rm -rf /var/lib/apt/lists/*

WORKDIR /app

RUN echo "Java version:" && java -version && \
	echo "\nMaven version:" && mvn -version && \
	echo "\nGit version:" && git --version

CMD ["/bin/bash"]
