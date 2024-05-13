# hk-project

## Runtime Environment
1. A machine with Docker and Docker-compose installed
2. Before running the program, make sure that port 8080 of the Docker host machine is not occupied. If you are not accessing the API interface within the host machine, port 8080 of the 
   host machine needs to be open to the outside.
3. The MySQL data files are mounted to the `/data/hkproject` directory on the Docker host, which therefore must be a Linux system that has a `/data` directory, and it may be necessary 
   to run the start.sh script with administrative privileges to have the permission to automatically create the `/data/hkproject` directory.
   If you are on a Mac or Windows system, you will need to modify the `docker-compose.yml` file, changing the `/data/hkproject` in the content below to the directory you need.
   ```
   volumes:
      - /data/hkproject:/var/lib/mysql
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
   ```
4. The Docker host machine needs to be able to connect to the Internet. This is because it needs to connect to Docker Hub to download images during deployment, and it needs to connect 
   to the Google Map API to calculate coordinate distances during runtime.
   
## Google Map API Key Replacement
1. Modify the contents of the`api-build/application.yml`file, replace the`Bpm12`in the following content with your valid Google Map API Key:
   ```
   google:
     api-key: Bpm12
   ```
   Please note this is in YAML format.

## Service Startup Steps
1. Replace the Google Map API key
2. Execute the`start.sh`script file in the root directory
   ```
   bash start.sh
   ```
   When the following similar content is displayed, it means that the program is running successfully:
   ```
   [+] Running 3/3
   Network hk-project-master_hkproject  Created                  0.2s 
   Container hkproject-mysql            Started                  0.4s 
   Container hk-project-master-app-1    Started                  0.9s
   ```

## Code Description
1. This project is based on SpringBoot web and uses Maven for building
2. The`src/../test`directory contains unit test cases and integration test cases. Test classes with the suffix 'UnitTest' are unit test classes, and other test classes are integration test 
   classes. During integration testing, H2 in-memory database is used.
3. This project took about 2 days to complete. As I am still employed, there are many areas that can be improved. For example, the order retrieval interface can use caching for performance 
   optimization, and the order addition interface can use existing calculated map coordinate data, so there is no need to call the Google Maps API every time, etc.
