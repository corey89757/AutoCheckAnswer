package com.corey;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            List<StandardAnswer> standardAnswers = stdAnswers.stream().map(line -> {
//                        System.out.println(line);
                        return new StandardAnswer(line.split(":")[0], Integer.valueOf(line.split(":")[1]));
                    }
            ).collect(Collectors.toList());


            List<Path> stuAnswerFileList = Files.list(Paths.get(stuAnswerFolder)).filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path path : stuAnswerFileList) {

                List<String> studentAnswers = null;
                try {
                    studentAnswers = Files.readAllLines(path.toAbsolutePath(), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    System.out.println("format error. path:" + path);
                    continue;
                }
                studentAnswers = studentAnswers.stream().filter(str -> str != null && !"".equals(str)).collect(Collectors.toList());
                int stdAnswerIndex = 0, stuAnswerIndex = 0;
                Integer total = 0;
                while (stdAnswerIndex < standardAnswers.size() && stuAnswerIndex < studentAnswers.size()) {
                    String curAnswer = studentAnswers.get(stuAnswerIndex);
                    StandardAnswer answer = standardAnswers.get(stdAnswerIndex);

                    //fix answer "，"->","
                    answer.setAnswer(answer.getAnswer().replaceAll("，", ","));

                    if (isContainChinese(answer.getAnswer())) {
                        if (curAnswer.contains(answer.getAnswer())) {
                            total += answer.getScore();
                        }
                    } else {
                        if (answer.getAnswer().contains("/")) {
                            total += answer.getScore();
                        } else {
                            if (answer.getAnswer().equalsIgnoreCase(curAnswer)) {
                                total += answer.getScore();
                            }
                        }
                    }

                    stdAnswerIndex++;
                    stuAnswerIndex++;
                }
                result.add(new StudentAnswerModel(path.getFileName().toString(), total));
            }

            result.forEach(o -> {
//                System.out.println(o.getName());
                System.out.format("%-10s%2s\n", o.getName().split("_")[1].split("\\.")[0], o.getTotalScore());
            });

            Files.write(Paths.get("./result"), result.stream().map(o -> o.getName() + ":" + o.getTotalScore()).collect(Collectors.joining("\n")).getBytes());

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
