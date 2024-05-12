# hk-project

## 运行环境
1. 安装了docker和docker-compose的机器
2. 运行程序前，docker宿主机的8080端口不要被占用；如果不是在宿主机内访问api接口的话，宿主机的8080端口要对外开放
3. docker的宿主机要能连接Internet，因为部署时，需要连接到dockerhup下载镜像，运行时，需要连接到Google Map Api计算坐标距离

## Google Map API key替换
1. 修改`api-build/application.yml`文件，将以下内容中的`Bpm12`修改成你的有效Google Map Api Key
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
   显示如下内容后，即表示程序运行成功

## 代码说明
1. 本项目基于springboot web，使用maven进行构建
2. `src/../test`目录下包含了单元测试用例和集成测试用例，以UnitTest为后缀的测试类是单元测试类，其他的测试类是集成测试类。集成测试时，使用了H2内存数据库
3. 本项目总计花费2天左右完成，因为本人还在职，还有很多地方可以改进，比如获取订单接口使用缓存进行性能优化，添加订单接口使用已有的计算过的地图坐标数据，不需要每次都调用Google地图api等等
