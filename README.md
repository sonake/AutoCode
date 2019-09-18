# AutoCode
代码生成starter启动器

PS:目前daimashengc仅支持mysql、oracle、sqlserver、postgresql四种数据库, 项目启动需要配置数据库连接
获取帮助
GitHub地址：https://github.com/sonake/AutoCode
如需关注项目最新动态，请Watch、Star项目，同时也是对项目最好的支持
QQ群：234786908(欢迎大家一起来交流划水姿势)
食用指南
第一步:请添加以下依赖 com.sonake autocode-spring-boot-starter 1.0.0 , com.sonake auto-code-ui 1.0.0
第二步，在配置文件添加自定义配置
#代码生成配置
auto-code:
  group-id: com.ac
  artifact-id: gc
  author: bby
  email: 123@bby
  #数据库类型
  database-type: mysql
  #模板路径
  vm-url: demo
  #表前缀
  table-prefix: t_
第三步:启动项目，访问ip:port/auto.html即可,若项目有权限验证, 请添加免认证路径：/auto.html,/auto/*
补充: 关于第三步： database-type参数，请指定数据库类型，参数为mysql、oracle、sqlserver、postgresql vm-url参数，支持用户自定义模板,用户只在resource下新建模板文件夹， 例如用户的模板位于 resource下的demo文件夹内，该参数值为：demo table-prefix参数，指用户需要忽略的表前缀 以上所有参数除database-type外，均可缺省
