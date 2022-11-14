package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import number.BonusNumber;
import number.WinningNumbers;
import type.Rank;

import java.util.*;

public class Application {
    public static void main(String[] args) {
        try {
            printInputMoneyComment();
            Money money = new Money(toInt(Console.readLine()));
            LottoGroups lottos = new LottoGroups(createLottos(money.getNumberToPublishLottos()));

            lottos.printAmountOfLottosComment();
            lottos.printAllLottos();

            printInputWinningNumbersComment();
            WinningNumbers winningNumbers = new WinningNumbers(toIntegers(spilt(Console.readLine())));

            printInputBonusNumberComment();
            BonusNumber bonusNumber = new BonusNumber(toInt(Console.readLine()));

            Map<Rank, Integer> numbersOfRanks = numbersOfRanks(lottos, winningNumbers.getWinningNumbers(), bonusNumber.getBonusNumber());

            printTotalResultComment(numbersOfRanks);
            printYieldComment(money, sumOfProceeds(numbersOfRanks));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printInputMoneyComment() {
        System.out.println("구입금액을 입력해 주세요.");
    }

    public static List<Lotto> createLottos(int numberToPublishLottos) {
        List<Lotto> lottos = new ArrayList<>();
        for (int i = 0; i < numberToPublishLottos; i++) {
            lottos.add(new Lotto(createRandomNumbers()));
        }
        return lottos;
    }

    private static List<Integer> createRandomNumbers() {
        return Randoms.pickUniqueNumbersInRange(1, 45, 6);
    }

    private static void printInputWinningNumbersComment() {
        System.out.println("당첨 번호를 입력해 주세요.");
    }

    private static void printInputBonusNumberComment() {
        System.out.println("보너스 번호를 입력해 주세요.");
    }

    private static int toInt(String input) {
        int number;
        try {
            number = Integer.parseInt(input);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[ERROR] 숫자만 입력해야 합니다.");
        }
        return number;
    }

    private static String[] spilt(String input) {
        if(!input.contains(","))
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 숫자 6개와 ,(쉼표)로만 작성해야 합니다.");
        return input.split(",");
    }

    private static List<Integer> toIntegers(String[] input) {
        List<Integer> result = new ArrayList<>();
        try {
            Arrays.stream(input).forEach(item -> result.add(Integer.parseInt(item)));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 숫자 6개와 ,(쉼표)로만 작성해야 합니다.");
        }
        return result;
    }

    public static Map<Rank, Integer> numbersOfRanks(LottoGroups lottos, List<Integer> winningNumbers, int bonusNumber) {
        List<Rank> ranks = lottos.getRanks(winningNumbers, bonusNumber);
        Map<Rank, Integer> numbersOfRanks = new HashMap<>();
        Arrays.stream(Rank.values()).forEach(rank -> numbersOfRanks.put(rank, Collections.frequency(ranks, rank)));

        return numbersOfRanks;
    }

    public static void printTotalResultComment(Map<Rank, Integer> numbersOfRanks) {
        System.out.println("당첨 통계\n---");
        Arrays.stream(Rank.values()).limit(5).sorted(Comparator.reverseOrder()).forEach(rank ->
                System.out.println(rank.getComment() + " - " + numbersOfRanks.get(rank) + "개"));
    }

    public static Long sumOfProceeds(Map<Rank, Integer> numbersOfRanks) {
        List<Long> proceeds = new ArrayList<>();
        Arrays.stream(Rank.values()).limit(5).forEach(rank ->
                proceeds.add((long) numbersOfRanks.get(rank) * rank.getPrice()));

        return proceeds.stream().mapToLong(proceed -> proceed).sum();
    }

    public static void printYieldComment(Money money, Long sumOfProceeds) {
        System.out.println("총 수익률은 " + money.calculateYield(sumOfProceeds) + "%입니다.");
    }
}
