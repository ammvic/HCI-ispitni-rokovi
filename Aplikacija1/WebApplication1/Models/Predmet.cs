namespace WebApplication1.Models
{
    public class Predmet
    {
        public int Id { get; set; }
        public string Naziv { get; set; }
        public int Semestar { get; set; }
        public ICollection<ProfesorPredmet> ProfesorPredmeti { get; set; }
    }
}