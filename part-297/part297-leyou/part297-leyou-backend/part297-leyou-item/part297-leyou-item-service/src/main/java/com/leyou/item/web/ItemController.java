package com.leyou.item.web;

import com.leyou.item.pojo.Item;
import com.leyou.item.service.ItemService;
import com.leyou.myexception.ExceptionEnum;
import com.leyou.myexception.LeyouException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item>  saveItem(Item item){
        // 校验商品的价格是否为空
        if(item.getPrice()==null){
            throw new LeyouException(ExceptionEnum.PRICE_CANNOT_BE_NULL);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Item item1 = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item1);
    }


}
