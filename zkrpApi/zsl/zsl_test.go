package zsl

import (
	//"encoding/json"
	"testing"

	"github.com/HachimanHiki/zkrpApi/zkrp"
)

func BenchmarkCommitter_GO(b *testing.B) {
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		zkrp.Committer(30)
	}
}

func BenchmarkProver_GO(b *testing.B) {
	commitmentPackage := zkrp.Committer(30)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		zkrp.Prover(commitmentPackage.Number, commitmentPackage.Confounding)
	}
}

// func BenchmarkVerifier_PY(b *testing.B) {
// 	commitmentPackage := zkrppy.Committer(30)
// 	proof := zkrppy.Prover(commitmentPackage.Number, commitmentPackage.Confounding)
// 	b.ResetTimer()
// 	for i := 0; i < b.N; i++ {
// 		result := zkrppy.Verifier(commitmentPackage.Commitment, proof)
// 		if !result {
// 			b.Error("Result is false")
// 		}
// 	}
// }

// func BenchmarkVerifier_GO(b *testing.B) {
// 	commitmentPackage := zkrp.Committer(30)
// 	proofBytes := zkrp.Prover(commitmentPackage.Number, commitmentPackage.Confounding)
// 	var proof zkrp.Proof
// 	if err := json.Unmarshal(proofBytes, &proof); err != nil {
// 		panic(err)
// 	}
// 	b.ResetTimer()
// 	for i := 0; i < b.N; i++ {
// 		result := zkrp.Verifier(commitmentPackage.Commitment[2:], proof)
// 		if !result {
// 			b.Error("Result is false")
// 		}
// 	}
// }

// func TestZKRP_PY(t *testing.T) {
// 	commitmentPackage := zkrppy.Committer(30)
// 	t.Logf("Number: 	 %d", commitmentPackage.Number)
// 	t.Logf("Confounding: %s", commitmentPackage.Confounding)
// 	t.Logf("Commitment:  %s", commitmentPackage.Commitment)

// 	proof := zkrppy.Prover(commitmentPackage.Number, commitmentPackage.Confounding)
// 	t.Logf("Proof:  	 %s", proof)

// 	result := zkrppy.Verifier(commitmentPackage.Commitment, proof)
// 	if !result {
// 		t.Error("Result is not true")
// 	}
// }

/*
func TestZKRP_GO(t *testing.T) {
	commitmentPackage := zkrp.Committer(30)
	t.Logf("Number:      %d", commitmentPackage.Number)
	t.Logf("Confounding: %s", commitmentPackage.Confounding)
	t.Logf("Commitment:  %s", commitmentPackage.Commitment)

	proofBytes := zkrp.Prover(commitmentPackage.Number, commitmentPackage.Confounding)
	var proof zkrp.Proof
	t.Logf("Proof:      %s", proof)
	if err := json.Unmarshal(proofBytes, &proof); err != nil {
		panic(err)
	}

	result := zkrp.Verifier(commitmentPackage.Commitment[2:], proof)
	if !result {
		t.Error("Result is not true")
	}
}
*/