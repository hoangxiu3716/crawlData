package de.gimik.apps.parsehub.backend.web.viewmodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.gimik.apps.parsehub.backend.model.Role;
import de.gimik.apps.parsehub.backend.model.User;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaViewInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmacyCrawlBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmaProductBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.user.UserViewInfo;

public abstract class TransferHelper {
    public static PageInfo convertToPageTransfer(final Page page) {
        PageInfo pageInfo = new PageInfo();
        if (page == null)
            return pageInfo;

        pageInfo.setData(page.getContent());
        pageInfo.setNumber(page.getNumber());
        pageInfo.setSize(page.getSize());
        pageInfo.setTotalElements(page.getTotalElements());
        pageInfo.setTotalPages(page.getTotalPages());

        return pageInfo;
    }

    public static PageInfo convertToUserInfoPage(Page<User> userPage) {
        PageInfo page = convertToPageTransfer(userPage);

        List<Object> userInfos = Lists.newArrayList(
                Iterables.transform(userPage.getContent(), new Function<User, Object>() {
                    @Override
                    public Object apply(User user) {
                        UserViewInfo userViewInfo = new UserViewInfo(user.getUsername(), user.getFullname(), createRoleMapForShow(user.getRoles()));
                        userViewInfo.setId(user.getId());
                        return userViewInfo;
                    }
                }));

        page.setData(userInfos);
        return page;
    }

    public static Map<String, Boolean> createRoleMapForShow(List<Role> datas) {
        Map<String, Boolean> roles = new HashMap<>();
        for (Role role : datas) {
            roles.put(role.getDescription(), Boolean.TRUE);
//            String role = authority.getAuthority();
//            role = role.toLowerCase();
//            role = role.replace("role_", "");
//            roles.put(role, Boolean.TRUE);
        }

        return roles;
    }
    
    public static Map<String, Boolean> createRoleMap(Collection<? extends GrantedAuthority> authorities) {
        Map<String, Boolean> roles = new HashMap<>();
        for (GrantedAuthority authority : authorities) {
            roles.put(authority.getAuthority(), Boolean.TRUE);
        }

        return roles;
    }

    public static PageInfo convertToRolePage(List<Role> roles) {
        PageInfo pageInfo = new PageInfo();

        List<Object> roleInfos = Lists.newArrayList(
                Iterables.transform(roles, new Function<Role, Object>() {
                    @Override
                    public Object apply(Role role) {
                        return role;
                    }
                }));

        pageInfo.setData(roleInfos);
        pageInfo.setNumber(0);
        pageInfo.setSize(roles.size());
        pageInfo.setTotalElements(roles.size());
        pageInfo.setTotalPages(1);

        return pageInfo;
    }
    public static PageInfo convertToPharmaCrawlPageInfo(Page<PharmacyCrawlBasicInfo> pageDatas ) {
        PageInfo page = convertToPageTransfer(pageDatas);
        page.setData(pageDatas.getContent());
        return page;
    }
    public static PageInfo convertToGroupPharmaPageInfo(Page<GroupPharmaViewInfo> pageDatas ) {
        PageInfo page = convertToPageTransfer(pageDatas);
        page.setData(pageDatas.getContent());
        return page;
    }
    public static PageInfo convertToProductPharmaPageInfo(Page<PharmaProductBasicInfo> pageDatas ) {
        PageInfo page = convertToPageTransfer(pageDatas);
        page.setData(pageDatas.getContent());
        return page;
    }
}
