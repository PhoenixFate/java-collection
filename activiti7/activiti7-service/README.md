# activiti7正式demo
## activiti7 7.1.0.M5数据库需要update一下
```
    ALTER TABLE `act_re_deployment` 
    ADD COLUMN `VERSION_` int(11) NULL DEFAULT NULL, 
    ADD COLUMN `PROJECT_RELEASE_VERSION_` varchar(255) NULL DEFAULT NULL;
```

