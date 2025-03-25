package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class SpendDbClient {

    private static Config CFG = Config.getInstance();

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();

    private final TransactionTemplate transactionTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    dataSource(CFG.authJdbcUrl())
            )
    );

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = categoryDaoJdbc.create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(
                    spendDao.create(spendEntity)
            );
        });
    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(categoryDaoJdbc.create(categoryEntity));
        });
    }

    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
            categoryDaoJdbc.deleteCategory(CategoryEntity.fromJson(category));
            return null;
        });
    }
}
