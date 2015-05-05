package io.github.robwin.swagger2markup.tasks
import io.github.robwin.markup.builder.MarkupLanguage
import io.github.robwin.swagger2markup.Swagger2MarkupConverter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class Swagger2MarkupTask extends DefaultTask {

    @Optional
    @InputDirectory
    def File inputDir

    @Optional
    @InputDirectory
    def File outputDir

    @Optional
    @Input
    MarkupLanguage markupLanguage;

    @Optional
    @InputDirectory
    def File examplesDir

    @Optional
    @InputDirectory
    def File descriptionsDir

    @Optional
    @InputDirectory
    def File schemasDir

    Swagger2MarkupTask() {
        inputDir = project.file('src/docs/swagger')
        markupLanguage = MarkupLanguage.ASCIIDOC
        outputDir = new File(project.buildDir, markupLanguage.toString().toLowerCase())
    }

    @TaskAction
    void convertSwagger2markup() {
        if (logger.isDebugEnabled()) {
            logger.debug("convertSwagger2markup task started")
            logger.debug("InputDir: {}", inputDir)
            logger.debug("OutputDir: {}", outputDir)
            logger.debug("ExamplesDir: {}", examplesDir)
            logger.debug("DescriptionsDir: {}", descriptionsDir)
            logger.debug("SchemasDir: {}", schemasDir)
            logger.debug("MarkupLanguage: {}", markupLanguage)
         }
        inputDir.eachFile { file ->
            if (logger.isDebugEnabled()) {
                logger.debug("File: {}", file.absolutePath)
            }
            Swagger2MarkupConverter.Builder builder = Swagger2MarkupConverter.from(file.absolutePath)
                    .withMarkupLanguage(markupLanguage);
            if(examplesDir){
                logger.debug("Include examples is enabled.")
                builder.withExamples(examplesDir.absolutePath)
            }
            if(descriptionsDir){
                logger.debug("Include descriptions is enabled.")
                builder.withDescriptions(descriptionsDir.absolutePath)
            }
            if(schemasDir){
                logger.debug("Include schemas is enabled.")
                builder.withSchemas(schemasDir.absolutePath)
            }
            builder.build().intoFolder(outputDir.absolutePath)
        }
        logger.debug("convertSwagger2markup task finished")
    }
}