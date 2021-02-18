package com.example.healthbank.DataAssets

import org.json.JSONObject

var eventUrl = ""
var eventType = ""
var eventData: JSONObject? = null

var eventInfo = ""
var eventName = ""
var eventRequired  = ""
var eventRequiredDetail = ""

//val zkrpDeclaration = "您的資料經過加密後 僅能得知您是否有資格本次活動 而不會洩露任何資訊"
val zkrpRiskDeclaration = "主辦單位不會取得您的個人就醫明細，僅提供加密處理後可驗證的密文格式，主辦單位僅會查看到驗證結果。"
val merkleTreeRiskDeclaration = "您的資料僅提供此臨床試驗計畫研究團隊使用，不會轉給其他單位或個人，請您放心。"

val dataUsageZkrpDeclaration = "僅作為本活動使用，主辦單位不會儲存您的任何個人就醫資料。"
val dataUsageMerkleTreeDeclaration = "本試驗預計於109年12月31日結束，計畫中止後，您的資料將一併刪除。"

val otherDetailZkrp = "本驗證方式可快速提供主辦單位您的健康情形是否適合參賽，若不參加驗證，請用紙本填寫相關資料。"
val otherDetailMerkleTree = "您可以到上方網站了解本試驗計畫之進度，若您決定撤回您的資料，可以隨時提出要求。"





val merkleTreeEmail = "456@gmail.com"
val zkrpEmail = "789@gmail.com"

val merkleTreePhoneNumber = "(02)-1234-XXXX"
val zkrpPhoneNumber = "(02)-5678-XXXX"

val merkleTreeUrl = "http://140.119.19.121:8080/share"
val zkrpUrl = "http://140.119.19.121:8080/marathon"