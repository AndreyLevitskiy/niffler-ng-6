package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategory;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    public final static Namespace NAMESPACE = create(CategoryExtension.class);
    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (anno.categories().length > 0) {
                        Category categoryAnno = anno.categories()[0];
                        CategoryJson category = new CategoryJson(
                                null,
                                categoryAnno.title().isEmpty() ? randomCategory() : anno.categories()[0].title(),
                                anno.username(),
                                categoryAnno.archived()
                        );
                        CategoryJson createdCategory = spendDbClient.createCategory(category);
                        context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(
                extensionContext.getUniqueId(),
                CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null) {
            spendDbClient.deleteCategory(category);
        }
    }
}