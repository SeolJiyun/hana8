package com;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class Emp {
	String name;
	String dept;
	int score;

	Emp(String name, String dept, int score) {
		this.name = name;
		this.dept = dept;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public String getDept() {
		return dept;
	}

	public int getScore() {
		return score;
	}

	public static void main(String[] args) {

		List<Emp> emps = Arrays.asList(
			new Emp("Hong", "Sales", 85),
			new Emp("Kim", "Sales", 95),
			new Emp("Choi", "HR", 55),
			new Emp("Nam", "HR", 75),
			new Emp("Lee", "IT", 82),
			new Emp("Park", "IT", 92),
			new Emp("Ahn", "Sales", 95)
		);

		Comparator<Emp> comp =
			Comparator.comparing(Emp::getScore, Comparator.reverseOrder())
				.thenComparing(Emp::getName);

        /* =========================
           ① 70점 미만 제외
           ========================= */
		System.out.println("=== 70점 이상 ===");

		emps.stream()
			.filter(e -> e.getScore() >= 70)
			.forEach(e ->
				System.out.println(e.getName() + "(" + e.getScore() + ")")
			);

        /* =========================
           ② 부서별 출력 (부서명 순)
           ========================= */
		System.out.println("\n=== 부서별 출력 (부서명 순) ===");

		emps.stream()
			.collect(Collectors.groupingBy(Emp::getDept))
			.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(entry -> {
				System.out.println("[" + entry.getKey() + "]");
				entry.getValue().forEach(e ->
					System.out.println(e.getName() + "(" + e.getScore() + ")")
				);
			});

        /* =========================
           ③ 부서별 최고 점수 1명
           ========================= */
		System.out.println("\n=== 부서별 최고 점수 ===");

		emps.stream()
			.collect(Collectors.groupingBy(
				Emp::getDept,
				Collectors.minBy(comp)
			))
			.forEach((dept, emp) -> {
				Emp e = emp.get();
				System.out.println(dept + ": " + e.getName() + "(" + e.getScore() + ")");
			});

        /* =========================
           ④ 부서 이름 역순 출력
           ========================= */
		System.out.println("\n=== 부서 역순 ===");

		emps.stream()
			.sorted(Comparator.comparing(Emp::getDept).reversed())
			.forEach(e ->
				System.out.println(e.getDept() + ": " + e.getName() + "(" + e.getScore() + ")")
			);

        /* =========================
           ⑤ 최종 종합 결과
           ========================= */
		System.out.println("\n=== 최종 결과 ===");

		emps.stream()
			.filter(e -> e.getScore() >= 70)
			.collect(Collectors.groupingBy(
				Emp::getDept,
				Collectors.minBy(comp)
			))
			.entrySet().stream()
			.sorted(Map.Entry.<String, Optional<Emp>>comparingByKey().reversed())
			.forEach(entry -> {
				Emp e = entry.getValue().get();
				System.out.println(
					entry.getKey() + ": " + e.getName() + "(" + e.getScore() + ")"
				);
			});
	}
}
