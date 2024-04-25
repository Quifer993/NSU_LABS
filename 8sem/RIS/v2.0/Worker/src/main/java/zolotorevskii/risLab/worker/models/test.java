package zolotorevskii.risLab.worker.models;

import org.paukov.combinatorics.CombinatoricsFactory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.paukov.combinatorics.util.ComplexCombinationGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {
    public static void main(String[] args) {
        var alphabet = "abcdefgi".split("");
        ICombinatoricsVector<String> alphabetInSplit = CombinatoricsFactory.createVector(alphabet);
        List<String> answers = new ArrayList<>();

        int maxLength = 3;
        int partCount = 2;
        int alphabetSize = alphabet.length;
        int partNumber = 2;
        int alphabetPartSize = alphabetSize/ partCount;

        String[] partAlp = Arrays.copyOfRange(alphabet,
                alphabetSize * (partNumber-1)/ partCount,
                alphabetSize * partNumber / partCount);

        // Генерируем комбинации, выбирая первый символ из первой половины алфавита
//
//        Generator<String> generator = new ComplexCombinationGenerator<String>();
//        for (ICombinatoricsVector<Character> vector : generator.variation(firstElement, otherElements)) {
//            // Делаем что-то с комбинацией (например, фильтруем или обрабатываем)
//            System.out.println(vector);
//        }
//
//        ICombinatoricsVector<String> vectorPart = CombinatoricsFactory.createVector(partAlp);
//        System.out.println();
    }

}
