package onlinemarket.controller.web;

import onlinemarket.model.Menu;
import onlinemarket.model.MenuGroup;
import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.service.MenuGroupService;
import onlinemarket.service.MenuService;
import onlinemarket.util.exception.menu.MenuNotFoundException;
import onlinemarket.util.exception.menuGroup.MenuGroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

@RestController
@RequestMapping("/api/menu")
public class ApiMenuController {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuGroupService menuGroupService;

    @RequestMapping(value = "/load/menu-group/{menuGroupId:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> loadMenuByMenuGroup(@PathVariable("menuGroupId") Integer id){
        MenuGroup menuGroup = menuGroupService.getByKey(id);
        if(menuGroup == null) return ResponseEntity.badRequest().body("Menu group not found.");
        return ResponseEntity.ok(menuService.list(menuGroup));
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public ResponseEntity<?> addMenu(@Validated(value = {Default.class, AdvancedValidation.AddNew.class}) @RequestBody Menu menu){
        try {
            menuService.save(menu);
            return ResponseEntity.ok(menu);
        } catch (MenuNotFoundException|MenuGroupNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ResponseEntity<?> updateMenu(@Validated(value = {Default.class, AdvancedValidation.AddNew.class}) @RequestBody Menu menu){
        try {
            menuService.update(menu);
            return ResponseEntity.ok(menu);
        } catch (MenuNotFoundException|MenuGroupNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/remove/{menuId:^\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> removeMenu(@PathVariable("menuId") Integer id){
        menuService.delete(id);
        return ResponseEntity.ok("ok");
    }
}
