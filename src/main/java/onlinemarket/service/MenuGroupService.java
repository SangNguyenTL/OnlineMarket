package onlinemarket.service;

import onlinemarket.form.filter.FilterForm;
import onlinemarket.model.MenuGroup;
import onlinemarket.result.ResultObject;

public interface MenuGroupService extends InterfaceService<Integer, MenuGroup>{
	ResultObject<MenuGroup> list(FilterForm filterForm);
}
