package com.ua.kpi.iasa.parallel_computing.lab0.utils;

import com.ua.kpi.iasa.parallel_computing.lab0.context.RunContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.util.Scanner;

public final class DataExportUtils {
    private static final String EXPORT_DIR_NAME = "export";
    private static final String ITERATION_TITLE = "Iteration";

    private static final String TEMP_SUFFIX = "_temp";

    public static void setupFile(RunContext runContext) {
        createExportDir();
        cleanExportFile(runContext.getExportFileName());
        createExportFile(runContext.getExportFileName());
        generateIterationColumn(runContext);
    }


    public static void setupFile(com.ua.kpi.iasa.parallel_computing.lab1.context.RunContext runContext) {
        createExportDir();
        cleanExportFile(runContext.getExportFileName());
        createExportFile(runContext.getExportFileName());
        generateIterationColumn(runContext);
    }

    private static void cleanExportFile(String exportFileName) {
        File file = Paths.get(getOutputDir(), exportFileName).toFile();
        file.delete();
    }

    private static void createExportFile(String exportFileName) {
        try {
            Paths.get(getOutputDir(), exportFileName).toFile().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createExportDir() {
        new File(getOutputDir()).mkdir();
    }

    public static String getOutputDir() {
        String projectPath = Paths.get("").toUri().getPath();
        return Paths.get(projectPath, EXPORT_DIR_NAME).toString();
    }

    public static void generateIterationColumn(com.ua.kpi.iasa.parallel_computing.lab1.context.RunContext context) {
        File file = Paths.get(getOutputDir(), context.getExportFileName()).toFile();
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(ITERATION_TITLE);
            fileWriter.write(",\n");

            for (int i = 1; i <= context.getIterationCount(); i++) {
                fileWriter.write(i + ",\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateIterationColumn(RunContext context) {
        File file = Paths.get(getOutputDir(), context.getExportFileName()).toFile();
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(ITERATION_TITLE);
            fileWriter.write(",\n");

            for (int i = 1; i <= context.getIterationCount(); i++) {
                fileWriter.write(i + ",\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addColumn(String exportFileName, String columnTitle, long[] columnData) {
        File file = Paths.get(getOutputDir(), exportFileName).toFile();

        String tempExportFileName = exportFileName + TEMP_SUFFIX;
        cleanExportFile(tempExportFileName);
        createExportFile(tempExportFileName);
        File tempExportFile = Paths.get(getOutputDir(), tempExportFileName).toFile();

        try (Scanner scanner = new Scanner(file);
             FileWriter fileWriter = new FileWriter(tempExportFile)) {
            if (scanner.hasNextLine()) {
                String titleLine = scanner.nextLine();
                titleLine += columnTitle + ",\n";
                fileWriter.write(titleLine);
            }

            for (long columnDatum : columnData) {
                String datum = new BigDecimal(columnDatum)
                        .setScale(3, RoundingMode.UP)
                        .divide(BigDecimal.valueOf(1e6), RoundingMode.UP)
                        .toString();
                if (scanner.hasNextLine()) {
                    String dataLine = scanner.nextLine();
                    dataLine += datum + ",\n";
                    fileWriter.write(dataLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        cleanExportFile(exportFileName);
        tempExportFile.renameTo(file);
    }

    private DataExportUtils() {
    }
}
