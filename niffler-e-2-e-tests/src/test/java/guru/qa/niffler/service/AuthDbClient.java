package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import static guru.qa.niffler.data.Databases.transaction;

public class AuthDbClient {

    private static Config CFG = Config.getInstance();

    public AuthUserJson createUser(AuthUserJson authUser) {
        return transaction(connection -> {
            AuthUserEntity ae = AuthUserEntity.fromJson(authUser);
            if (ae.getUsername() == null) {
                AuthUserEntity ae = new CategoryDaoJdbc(connection).create(spendEntity.getCategory());
                ae.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(
                    new SpendDaoJdbc(connection).create(spendEntity)
            );
        }, CFG.spendJdbcUrl());
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
        }, CFG.spendJdbcUrl());
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
            new CategoryDaoJdbc(connection).deleteCategory(CategoryEntity.fromJson(category));
        }, CFG.spendJdbcUrl());
    }
}
