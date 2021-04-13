using System;
using System.ComponentModel.DataAnnotations;

namespace WebApplication2
{
    public class Wallet
    {
        [Required]
        [Key]
        public int WalletAddress { get; set; }
        [Required]
        [StringLength(128)]
        public string Cryptocurrency { get; set; }
        public int Amount { get; set; }

        public Wallet(int id, string crypto, int value)
        {
            WalletAddress = id;
            Cryptocurrency = crypto;
            Amount = value;
        }
    }
}
