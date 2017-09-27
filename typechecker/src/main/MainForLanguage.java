package main;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.compiler.typechecker.TypeChecker;
import org.eclipse.ceylon.compiler.typechecker.TypeCheckerBuilder;
import org.eclipse.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import org.eclipse.ceylon.compiler.typechecker.io.cmr.impl.LeakingLogger;

/**
 * Some hack before a proper unit test harness is put in place
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForLanguage {
    /**
     * Files that are not under a proper module structure are 
     * placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {
        ClosableVirtualFile latestZippedLanguageSourceFile = 
                MainHelper.getLatestZippedLanguageSourceFile();
        RepositoryManager repositoryManager = CeylonUtils.repoManager()
                .systemRepo("../dist/dist/repo")
                .logger(new LeakingLogger())
                .buildManager();
        TypeChecker typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory(latestZippedLanguageSourceFile)
                .setRepositoryManager(repositoryManager)
                .getTypeChecker();
        typeChecker.process();
        latestZippedLanguageSourceFile.close();
        
        if (typeChecker.getErrors() > 0) {
            System.exit(1);
        }
    }

}
