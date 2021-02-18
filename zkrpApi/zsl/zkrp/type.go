package zkrp

import (
	"math/big"
)

// CommitmentPackage for commitment json
type CommitmentPackage struct {
	Number      int    `json:"number"`
	Confounding string `json:"confounding"`
	Commitment  string `json:"commitment"`
}

// Proof ...
type Proof struct {
	C   *big.Int   `json:"c"`
	CP  *big.Int   `json:"cP"`
	CPP *big.Int   `json:"cPP"`
	C1P *big.Int   `json:"c1P"`
	C2P *big.Int   `json:"c2P"`
	R   *big.Int   `json:"R"`
	EL  []*big.Int `json:"el"`
	SQ1 []*big.Int `json:"sq1"`
	SQ2 []*big.Int `json:"sq2"`
}

// NewCommitmentPackage ...
func NewCommitmentPackage(number int, confounding string, commitment string) CommitmentPackage {
	return CommitmentPackage{number, confounding, commitment}
}

// NewProof ...
func NewProof(c *big.Int, cP *big.Int, cPP *big.Int, c1P *big.Int, c2P *big.Int, r *big.Int, el []*big.Int, sq1 []*big.Int, sq2 []*big.Int) Proof {
	return Proof{c, cP, cPP, c1P, c2P, r, el, sq1, sq2}
}
