# TC Tests

## Pre-conditions
- Install Docker on your machine [Install Docker Engine](https://docs.docker.com/engine/install)
- Create a Docker network 'selenoid' via `docker network create selenoid`
- Run the docker-compose file 'docker-compose.yml' via the command: `docker compose up -d`
- Wait until the TC server is established
- Choose 'HDBC' db driver during the initial server setup
- Create the Admin with the following credentials: "admin:admin"
- Read a Super user token from the './teamcity-server/logs/teamcity-server.log' file and copy it to the buffer

### How to run
- Execute the following command `mvn test -Dtc.server.token=<your_superuser_token> && mvn allure:serve'
- An Allure Report will be opened

### Monitoring ###
- Selenoid UI is available by the address: 'http://localhost:8083' with VNC
- To turn on video recording: use '-Dtc.server.remote.video-recording=true' during the tests launch