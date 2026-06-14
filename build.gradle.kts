// Antというビルドツールが持っているReplaceTokensを利用する
// Welcome to @THEME_PARK_NAME@ を Welcome to Grelephant's Wonder Worldのように置換できる
import org.apache.tools.ant.filters.ReplaceTokens

// baseプラグインを適用している
// clean、assemble、buildなどの基本的なタスクを追加する
// gradle cleanでbuildフォルダを削除する
plugins {
  base
}

// Taskを登録している
// タスク名はgenerateDescriptions
tasks.register<Copy>("generateDescriptions") {
  // コピー元
  from("descriptions")
  // コピー先
  // layout.buildDirectoryはbuild/を指します
  // なのでコピー先は build/descriptions/
  into(layout.buildDirectory.dir("descriptions"))
  // ファイルに記載された変数を文字列に置換している
  filter<ReplaceTokens>("tokens" to mapOf("THEME_PARK_NAME" to "Grelephant's Wonder World"))
}
