package com.corey;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujian
 * @date 2020/06/22
 */
public class AutoCheckAnswer {

    public static void main(String[] args) {
        String stuAnswerFolder = args[0];//student folder
        String stdAnswerFile = args[1];//answer file. format: answer   score
        doCheck(stuAnswerFolder, stdAnswerFile);
    }

    public static void doCheck(String stuAnswerFolder, String stdAnswerFile) {
        try {
            List<StudentAnswerModel> result = new ArrayList<>();

            List<String> stdAnswers = Files.readAllLines(Paths.get(stdAnswerFile), StandardCharsets.UTF_8);
            List<StandardAnswer> standardAnswers = stdAnswers.stream().map(line -> new StandardAnswer(line.split("\t")[0], Integer.valueOf(line.split("\t")[1])))
                    .collect(Collectors.toList());

            List<Path> stuAnswerFileList = Files.list(Paths.get(stuAnswerFolder)).filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path path : stuAnswerFileList) {
                List<String> studentAnswers = Files.readAllLines(path.toAbsolutePath(), StandardCharsets.UTF_8);
                int stdAnswerIndex = 0, stuAnswerIndex = 0;
                Integer total = 0;
                while (stdAnswerIndex < standardAnswers.size() && stuAnswerIndex < studentAnswers.size()) {
                    String curAnswer = studentAnswers.get(stuAnswerIndex);
                    StandardAnswer answer = standardAnswers.get(stdAnswerIndex);

                    if (answer.getAnswer().equals(curAnswer)) {
                        total += answer.getScore();
                    }
                    stdAnswerIndex++;
                    stuAnswerIndex++;
                }
                result.add(new StudentAnswerModel(path.getFileName().toString(), total));
            }

            result.forEach(o -> System.out.println(o.getName() + ":" + o.getTotalScore()));

            Files.write(Paths.get("./result"), result.stream().map(o -> o.getName() + ":" + o.getTotalScore()).collect(Collectors.joining("\n")).getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
