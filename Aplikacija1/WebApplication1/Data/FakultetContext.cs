using Microsoft.EntityFrameworkCore;
using WebApplication1.Models;

namespace WebApplication1.Data
{
    public class FakultetContext : DbContext
    {
        public FakultetContext(DbContextOptions<FakultetContext> options) : base(options)
        {
        }

        public DbSet<Profesor> Profesori { get; set; }
        public DbSet<Predmet> Predmeti { get; set; }
        public DbSet<ProfesorPredmet> ProfesorPredmeti { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<ProfesorPredmet>()
                .HasKey(pp => new { pp.ProfesorId, pp.PredmetId });

            modelBuilder.Entity<ProfesorPredmet>()
                .HasOne(pp => pp.Profesor)
                .WithMany(p => p.ProfesorPredmeti)
                .HasForeignKey(pp => pp.ProfesorId);

            modelBuilder.Entity<ProfesorPredmet>()
                .HasOne(pp => pp.Predmet)
                .WithMany(p => p.ProfesorPredmeti)
                .HasForeignKey(pp => pp.PredmetId);
        }
    }
}
