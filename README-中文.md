# hk-project

## 运行环境
1. 安装了docker和docker-compose的机器
2. 运行程序前，docker宿主机的8080端口不要被占用；如果不是在宿主机内访问api接口的话，宿主机的8080端口要对外开放
3. MySQL的数据文件挂载到docekr宿主机的/data/hkproject目录下，因此宿主机需是linux系统，需要有/data目录，且可能需要以管理员身份运行`start.sh`脚本才有权限自动创建/data/hkproject目录，
   如果是mac或windows系统，需要修改docker-compose.yml中的挂载目录，将下面内容中的`/data/hkproject`修改成你需要的目录
   ```
   volumes:
      - /data/hkproject:/var/lib/mysql
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
   ```
4. docker的宿主机要能连接Internet，因为部署时，需要连接到dockerhup下载镜像，运行时，需要连接到Google Map Api计算坐标距离

## Google Map API key替换
1. 修改`api-build/application.yml`文件的内容，将以下内容中的`Bpm12`修改成你的有效Google Map Api Key
   ```
   google:
     api-key: Bpm12
   ```
   注意这是yaml格式

## 服务启动步骤
1. 替换Google Map API key
2. 执行根目录下的`start.sh`脚本文件
   ```
   bash start.sh
   ```
   显示如下类似内容后，即表示程序运行成功
   ```
   [+] Running 3/3
   Network hk-project-master_hkproject  Created                  0.2s 
   Container hkproject-mysql            Started                  0.4s 
   Container hk-project-master-app-1    Started                  0.9s
   ```

## 代码说明
1. 本项目基于springboot web，使用maven进行构建
2. `src/../test`目录下包含了单元测试用例和集成测试用例，以UnitTest为后缀的测试类是单元测试类，其他的测试类是集成测试类。集成测试时，使用了H2内存数据库
3. 本项目总计花费2天左右完成，因为本人还在职，还有很多地方可以改进，比如获取订单接口使用缓存进行性能优化，添加订单接口使用已有的计算过的地图坐标数据，不需要每次都调用Google地图api等等
