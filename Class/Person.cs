using System;
using System.ComponentModel.DataAnnotations;

namespace AzureSamples.AzureSQL.Class
{
    public class Person
    {
        [Key]
        public int PersonId { get; set; }
        [Required]
        [StringLength(128)]
        public string Username { get; set; }
        [Required]
        public Wallet BitcoinWalletAddress { get; set; }
        [Required]
        public Wallet EtheriumWalletAddress { get; set; }
        [Required]
        public Wallet CardanoWalletAddress { get; set; }
        [Required]
        public Wallet BinanceWalletAddress { get; set; }
        [Required]
        public Wallet LitecoinWalletAddress { get; set; }

        public Person(int id, string name, Wallet btc, Wallet eth, Wallet ada, Wallet bnc, Wallet ltc)
        {
            PersonId = id;
            Username = name;
            BitcoinWalletAddress = btc;
            EtheriumWalletAddress = eth;
            CardanoWalletAddress = ada;
            BinanceWalletAddress = bnc;
            LitecoinWalletAddress = ltc;
        }
    }
}
