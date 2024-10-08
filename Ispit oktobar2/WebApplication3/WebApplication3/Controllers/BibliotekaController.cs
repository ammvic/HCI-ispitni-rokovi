using Microsoft.AspNetCore.Mvc;
using WebApplication3.Models;
using Microsoft.EntityFrameworkCore;
using WebApplication3.Dtos;


namespace WebApplication3.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class KnjigeController : ControllerBase
    {
        private readonly AplikacijaContext _context;

        public KnjigeController(AplikacijaContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<KnjigaDTO>>> GetKnjige()
        {
            var knjige = await _context.Knjige.Select(k => new KnjigaDTO
            {
                Id= k.Id,
                Naslov = k.Naslov,
                Autor = k.Autor,
                Zanr = k.Zanr
            }).ToListAsync();

            return Ok(knjige);
        }


        [HttpGet("search")]
        public async Task<ActionResult<IEnumerable<Knjiga>>> SearchKnjige(string pretraga = null)
        {
            var knjigeQuery = _context.Knjige.AsQueryable();

            if (!string.IsNullOrEmpty(pretraga))
            {
                knjigeQuery = knjigeQuery.Where(k =>
                    k.Naslov.Contains(pretraga) ||
                    k.Autor.Contains(pretraga) ||
                    k.Zanr.Contains(pretraga));
            }

            var knjige = await knjigeQuery.ToListAsync();

            if (!knjige.Any())
            {
                return NotFound("Nema knjiga koje odgovaraju pretrazi.");
            }

            return Ok(knjige);
        }


        // Detalji pojedinačne knjige
        [HttpGet("{id}")]
        public async Task<ActionResult<Knjiga>> GetKnjiga(int id)
        {
            var knjiga = await _context.Knjige.FindAsync(id);

            if (knjiga == null)
            {
                return NotFound();
            }

            return knjiga;
        }
    }

}
