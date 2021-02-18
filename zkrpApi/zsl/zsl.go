package zsl

import (
	"encoding/json"

	"github.com/HachimanHiki/zkrpApi/zsl/zkrp"
)

// Committer amount
func Committer(number int) zkrp.CommitmentPackage {
	return zkrp.Committer(number)
}

// Prover commitment in range
func Prover(number, lowerbound, upperbound int64, confounding string) string {
	return zkrp.Prover(number, lowerbound, upperbound, confounding[2:])
}

// Verifier is commitment in range with proof
func Verifier(commitment string, lowerbound, upperbound int64, proofBytes []byte) bool {
	var proof zkrp.Proof
	if err := json.Unmarshal(proofBytes, &proof); err != nil {
		panic(err)
	}
	return zkrp.Verifier(commitment[2:], lowerbound, upperbound, proof)
}

// AddCommitment add commitment with mul and mod
func AddCommitment(commitmentA, commitmentB string) string {
	return zkrp.AddCommitment(commitmentA[2:], commitmentB[2:])
}

// AddConfounding add two confounding
func AddConfounding(confoundingA, confoundingB string) string {
	return zkrp.AddConfounding(confoundingA[2:], confoundingB[2:])
}

// Check is commitmentPackage are correct
func Check(number int, confounding string, commitment string) bool {
	return zkrp.Check(number, confounding[2:], commitment[2:])
}

// CheckAmountCommitment check is sun of two commitment plain number is zero
func CheckAmountCommitment(commitmentA, commitmentB string, totalConfounding string) bool {
	return zkrp.CheckAmountCommitment(commitmentA[2:], commitmentB[2:], totalConfounding[2:])
}
