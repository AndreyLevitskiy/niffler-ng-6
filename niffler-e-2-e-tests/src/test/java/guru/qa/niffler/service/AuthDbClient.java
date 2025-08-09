package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.AuthUserJson;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(CFG.authJdbcUrl());

    public AuthUserJson createUser(AuthUserJson authUser) {
        return jdbcTxTemplate.execute(() -> {
            var entity = AuthUserEntity.fromJson(authUser);
            var saved  = new AuthUserDaoJdbc().create(entity);
            return AuthUserJson.fromEntity(saved);
        });
    }

//    public CategoryJson createCategory(CategoryJson category) {
//        return jdbcTxTemplate.execute(() -> {
//            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
//            return CategoryJson.fromEntity(new CategoryDaoJdbc().create(categoryEntity));
//        });
//    }
//
//    public void deleteCategory(CategoryJson category) {
//        return jdbcTxTemplate.execute(() -> {
//            new CategoryDaoJdbc().deleteCategory(CategoryEntity.fromJson(category));
//        });
//    }
}
