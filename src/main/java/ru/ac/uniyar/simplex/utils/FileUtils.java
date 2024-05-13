package ru.ac.uniyar.simplex.utils;

import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.exceptions.BasesFormatException;
import ru.ac.uniyar.simplex.exceptions.EmptyFileException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static TaskEntity getTaskFromFile(String filePath) {
        try {
            List<String> data = readFile(filePath);
            printFile(data);

            String[] sizes = data.get(0).split("\\s*x\\s*");
            if (sizes.length != 2) {
                throw new EmptyFileException("Недостаточно или избыток данных о размерности матрицы!");
            }
            int rows = Integer.parseInt(sizes[0]);
            int columns = Integer.parseInt(sizes[1]);

            ArrayList<String> coefficients = new ArrayList<>();
            for (int i = 1; i <= rows; i++) {
                coefficients.add(data.get(i));
            }
            Fraction[][] matrix = createMatrix(coefficients, columns);

            ArrayList<Integer> bases = getBases(data, rows, columns);

            return new TaskEntity();

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Ошибка при считывании чисел: " + e.getMessage());
        } catch (BasesFormatException e) {
            System.err.println("Ошибка при считывании базисных переменных: " + e.getMessage());
        } catch (EmptyFileException e) {
            System.err.println("Недостаточно данных: " + e.getMessage());
        }
        return null;
    }

    private static List<String> readFile(String filePath) throws IOException {
        return Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8);
    }

    private static Fraction[][] createMatrix(List<String> coefficients, int columns) throws NumberFormatException, EmptyFileException {
        Fraction[][] matrix = new Fraction[coefficients.size()][];
        for (int i = 0; i < coefficients.size(); i++) {
            String[] values = coefficients.get(i).split("\\s+");
            if (values.length != columns) {
                throw new EmptyFileException("Недостаточно или избыток коэффициентов!");
            }
            matrix[i] = new Fraction[values.length];
            for (int j = 0; j < values.length; j++) {
                String[] fractionParts = values[j].split("/");
                if (fractionParts.length == 1) {
                    matrix[i][j] = new Fraction(Integer.parseInt(fractionParts[0]), 1);
                } else {
                    matrix[i][j] = new Fraction(Integer.parseInt(fractionParts[0]), Integer.parseInt(fractionParts[1]));
                    matrix[i][j].reduction();
                }
            }
        }
        return matrix;
    }

    private static ArrayList<Integer> getBases(List<String> data, int rows, int columns) throws BasesFormatException, EmptyFileException {
        String[] basesStr = data.get(rows + 1).split("\\s*,\\s*");
        ArrayList<Integer> bases = new ArrayList<>();
        for (String s : basesStr) {
            bases.add(Integer.parseInt(s));
            if (bases.get(bases.size() - 1) > columns) {
                throw new BasesFormatException("Номер базисной переменной больше кол-ва столбцов!");
            }
        }
        if (bases.size() != rows) {
            throw new EmptyFileException("Кол-во базисных переменных не равно кол-ву строк матрицы!");
        }
        return bases;
    }

    private static void printFile(List<String> lines) throws EmptyFileException {
        System.out.println("\nСчитано из файла: ");
        if (lines.isEmpty()) {
            throw new EmptyFileException("Файл пуст!");
        } else {
            for (String line : lines) {
                System.out.println(line);
            }
        }
    }
}
