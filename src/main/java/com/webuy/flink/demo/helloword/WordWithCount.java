package com.webuy.flink.demo.helloword;

public class WordWithCount extends Object{
		public String word;

		public long count;

		public WordWithCount(String word, long count) {
			this.word = word;
			this.count = count;
		}

	public WordWithCount() {
	}

	@Override
		public String toString() {
			return "WordWithCount{" +
					"word='" + word + '\'' +
					", count=" + count +
					'}';
		}
	}