package onlinemarket.service;

import onlinemarket.dao.MenuDao;
import onlinemarket.dao.MenuGroupDao;
import onlinemarket.dao.config.MenuPositionConfigDao;
import onlinemarket.form.config.MenuPositionConfig;
import onlinemarket.model.Menu;
import onlinemarket.model.MenuGroup;
import onlinemarket.util.Help;
import onlinemarket.util.MenuSite;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service("menuSiteService")
public class MenuSiteServiceImpl implements MenuSiteService{

    @Autowired
    MenuPositionConfigDao menuPositionConfigDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuDao menuDao;

    private String path;

    @Autowired
    private ServletContext servletContext;

    private String templateMenuTop(Menu menu){
        String pathMenu = StringUtils.contains(menu.getPath(), "http") ? menu.getPath() : path+menu.getPath();
        return "<li><a alt=\""+menu.getDescription()+"\" href=\""+pathMenu+"\"><i class=\"fa "+menu.getIcon()+"\"></i> "+menu.getName()+"</a></li>";
    }

    private String templateHeader(Menu menu){
        Set<Menu> menuList = menu.getMenus();
        String pathMenu = StringUtils.contains(menu.getPath(), "http") ? menu.getPath() : path+menu.getPath();
        String html = "<li class=\""+(menuList.isEmpty() ? "" : "dropdown")+" information-link\"><a href=\""+pathMenu+"\"><i class=\"fa "+menu.getIcon()+"\"></i> " +menu.getName()+"</a>\n";
        html += childTemplate(menu);
        html +="</li>";
        return html;
    }

    private String childTemplate(Menu menu){

        Set<Menu> menuList = menu.getMenus();
        StringBuilder html = new StringBuilder();
        if(!menuList.isEmpty()){
            html.append("<div class=\"dropdown-menu\">\n");
            html.append("<ul>");
            Iterator<Menu> menuIterator =  menuList.iterator();
            while (menuIterator.hasNext()){
                Menu childMenu = menuIterator.next();
                String pathMenu = StringUtils.contains(menu.getPath(), "http") ? childMenu.getPath() : path+childMenu.getPath();
                html.append("<li><a alt=\"").append(menu.getDescription()).append("\" href=\"").append(pathMenu).append("\">").append("<i class=\"fa ").append(childMenu.getIcon()).append("\"></i> ").append(childMenu.getName()).append("</a>");
                html.append(childTemplate(childMenu));
                html.append("</li>");
            }
            html.append("</ul>");
            html.append("</div>");
        }

        return html.toString();
    }

    private String templateFooter(String id){
        StringBuilder html = new StringBuilder();
        if(StringUtils.isNotBlank(id) && Help.isInteger(id)){
            MenuGroup menuGroup = menuGroupDao.getByKey(Integer.valueOf(id));
            if(menuGroup != null){
                html.append("<h̀5>").append(menuGroup.getName()).append("</h5>");;
                List<Menu> menuList = menuDao.listByMenuGroup(menuGroup);
                if(!menuList.isEmpty()){
                    html.append("<ul>");
                    for (Menu aMenuList : menuList) {
                        html.append(templateMenuTop(aMenuList));
                    }
                    html.append("</ul>");
                }
            }
        }
        return html.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public MenuSite generateMenu() {
        path = servletContext.getContextPath();

        MenuSite menuSite = new MenuSite();

        MenuPositionConfig menuPositionConfig = menuPositionConfigDao.getConfiguration(new MenuPositionConfig());

        if(StringUtils.isNotBlank(menuPositionConfig.getTop()) && Help.isInteger(menuPositionConfig.getTop())){
            MenuGroup menuGroup = menuGroupDao.getByKey(Integer.valueOf(menuPositionConfig.getTop()));
            if(menuGroup != null){
                List<Menu> menuList = menuDao.listByMenuGroup(menuGroup);
                StringBuilder top = new StringBuilder();
                for (Menu aMenuList : menuList) {
                    top.append(templateMenuTop(aMenuList)).append("\n");
                }
                menuSite.setTop(top.toString());
            }
        }

        if(StringUtils.isNotBlank(menuPositionConfig.getHeader()) && Help.isInteger(menuPositionConfig.getHeader())){
            MenuGroup menuGroup = menuGroupDao.getByKey(Integer.valueOf(menuPositionConfig.getHeader()));
            if(menuGroup != null){
                List<Menu> menuList = menuDao.listByMenuGroup(menuGroup);
                StringBuilder header = new StringBuilder();
                for (Menu aMenuList : menuList) {
                    header.append(templateHeader(aMenuList));
                }
                menuSite.setHeader(header.toString());
            }
        }

        menuSite.setFooterFirst(templateFooter(menuPositionConfig.getFooterFirst()));
        menuSite.setFooterSecond(templateFooter(menuPositionConfig.getFooterSecond()));
        menuSite.setFooterThird(templateFooter(menuPositionConfig.getFooterThird()));
        menuSite.setFooterFour(templateFooter(menuPositionConfig.getFooterFour()));

        menuSite.setFooterBottomRight(generateFooterBottom(menuPositionConfig.getFooterBottomRight()));
        menuSite.setFooterBottom(generateFooterBottom(menuPositionConfig.getFooterBottom()));

        return menuSite;
    }

    private String generateFooterBottom(String id){
        StringBuilder html = new StringBuilder();
        if(StringUtils.isNotBlank(id) && Help.isInteger(id)){
            MenuGroup menuGroup = menuGroupDao.getByKey(Integer.valueOf(id));
            if(menuGroup != null){
                List<Menu> menuList = menuDao.listByMenuGroup(menuGroup);
                for(Menu menu : menuList) {
                    String pathMenu = StringUtils.contains(menu.getPath(), "http") ? menu.getPath() : path + menu.getPath();
                    html.append("<a href=\"").append(pathMenu).append("\" target=\"_blank\">").append("<i class=\"fa ").append(menu.getIcon()).append("\"></i> ").append(menu.getName()).append("</a>");
                }
            }
        }
        return html.toString();
    }
}
